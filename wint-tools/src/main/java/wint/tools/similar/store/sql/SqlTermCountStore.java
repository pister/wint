package wint.tools.similar.store.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import wint.tools.similar.store.TermCountStore;

/**
 * @author pister
 * 2012-8-22 上午10:50:47
 */
/*

create table similar_term_count (
	_id bigint primary key auto_increment,
	group_id varchar(100) not null,
	term varchar(50) not null,
	term_count bigint not null,
	gmt_create datetime not null,
	gmt_modified datetime not null	
);
create index term_count_1 on similar_term_count(group_id, term);

*/
public class SqlTermCountStore extends SqlBase implements TermCountStore {

	public void addTermCount(String groupId, String term, int count) {
		if (!updateCount(groupId, term, count)) {
			insertCount(groupId, term, count);
		}
	}
	
	private void insertCount(String groupId, String term, int count) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			String sql = 
					"insert into similar_term_count (group_id, term, term_count, gmt_create, gmt_modified) values(?,?,?,now(),now())";
			conn = this.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, 	groupId);
			pstmt.setString(2,	term);
			pstmt.setInt(	3, 	count);
			pstmt.executeUpdate();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			close(pstmt);
			close(conn);
		}
	}
	
	private boolean updateCount(String groupId, String term, int count) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			String sql = 
				"update similar_term_count set term_count = term_count + ?, gmt_modified = now() where group_id=? and term=? ";
			conn = this.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(	1, 	count);
			pstmt.setString(2, 	groupId);
			pstmt.setString(3,	term);
			return (pstmt.executeUpdate() > 0);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			close(pstmt);
			close(conn);
		}
	}

	public long getTermCount(String groupId, String term) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "select term_count from similar_term_count where group_id=? and term=? ";
			conn = this.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, 	groupId);
			pstmt.setString(2,	term);
			rs = pstmt.executeQuery();
			long count = 0;
			if (rs.next()) {
				count = rs.getLong(1);
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

}
