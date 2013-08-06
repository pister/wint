package wint.tools.similar.store.sql;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

import wint.tools.similar.proccess.ContentTerm;
import wint.tools.similar.proccess.SimilarTerm;
import wint.tools.similar.store.ContentTermStore;
import wint.tools.similar.store.SimilarTermSerializeService;
import wint.tools.util.CloseableIterator;
import wint.tools.util.FastByteArrayInputStream;



/**
 * @author pister
 * 2012-8-21 下午6:42:54
 */
/*

create table similar_content_term (
	_id bigint primary key auto_increment,
	group_id varchar(100) not null,
	content_id varchar(1024) not null,
	term_total int not null,
	sqrt_result decimal(10,6) not null,
	similar_term mediumblob,
	gmt_create datetime not null,
	gmt_modified datetime not null	
);
create index content_term_1 on similar_content_term(group_id, content_id);

*/
// similar_term放入一个大字段
public class SqlContentTermStore extends SqlBase implements ContentTermStore {
	
	private SimilarTermSerializeService serializeService = new SimilarTermSerializeService();
	
	public void save(String groupId, String id, ContentTerm contentTerm) {
		if (!update(groupId, id, contentTerm)) {
			insert(groupId, id, contentTerm);
		}
	}

	public boolean update(String groupId, String id, ContentTerm contentTerm) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			String sql = 
				"update similar_content_term set term_total = ?, sqrt_result = ?, similar_term = ?, gmt_modified = now() where group_id=? and content_id=? ";
			conn = this.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(	1, 	contentTerm.getTermTotal());
			pstmt.setDouble(2, 	contentTerm.getSqrtResult());
			pstmt.setBlob(	3, toStream(contentTerm.getSimilarTerms()));
			pstmt.setString(4, groupId);
			pstmt.setString(5, id);
			return pstmt.executeUpdate() > 0;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			close(pstmt);
			close(conn);
		}
	}
	
	protected InputStream toStream(Map<String, SimilarTerm> similarTerms) {
		return new FastByteArrayInputStream(serializeService.serialize(similarTerms));
	}

	public void insert(String groupId, String id, ContentTerm contentTerm) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			String sql = 
				"insert into similar_content_term (group_id, content_id, term_total, sqrt_result, similar_term, gmt_create, gmt_modified) values(?,?,?,?,?,now(),now())";
			conn = this.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, 	groupId);
			pstmt.setString(2, 	id);
			pstmt.setInt(	3, 	contentTerm.getTermTotal());
			pstmt.setDouble(4, 	contentTerm.getSqrtResult());
			pstmt.setBlob(	5, 	toStream(contentTerm.getSimilarTerms()));
			pstmt.executeUpdate();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			close(pstmt);
			close(conn);
		}
	}

	public int getContentTermCount(String groupId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "select count(1) from similar_content_term where group_id = ?";
			conn = this.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, 	groupId);
			rs = pstmt.executeQuery();
			int count = 0;
			if (rs.next()) {
				count = rs.getInt(1);
			} 
			return count;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			close(rs);
			close(pstmt);
			close(conn);
		}
	}

	public CloseableIterator<ContentTerm> getContentTerms(String groupId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "select _id, group_id, content_id, term_total, sqrt_result, similar_term from similar_content_term where group_id = ?";
			conn = this.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, 	groupId);
			rs = pstmt.executeQuery();
			rs.setFetchSize(1);
			return new SqlResultCloseableIterator(serializeService, rs, pstmt, conn);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
