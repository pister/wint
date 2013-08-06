package wint.tools.similar.store.sql;

import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import wint.tools.similar.proccess.ContentTerm;
import wint.tools.similar.proccess.SimilarTerm;
import wint.tools.similar.store.SimilarTermSerializeService;
import wint.tools.util.CloseableIterator;
import wint.tools.util.FastByteArrayOutputStream;
import wint.tools.util.IoUtil;
import wint.tools.util.SqlUtil;

public class SqlResultCloseableIterator implements CloseableIterator<ContentTerm> {

	private SimilarTermSerializeService serializeService;
	
	private ResultSet rs;
	
	private Statement stmt;

	private Connection conn;
	
	public SqlResultCloseableIterator(SimilarTermSerializeService serializeService, ResultSet rs, Statement stmt, Connection conn) {
		super();
		this.serializeService = serializeService;
		this.rs = rs;
		this.stmt = stmt;
		this.conn = conn;
	}

	public boolean hasNext() {
		try {
			return rs.next();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public ContentTerm next() {
		try {
			String contentId = rs.getString("content_id");
			int termTotal = rs.getInt("term_total");
			double sqrtResult = rs.getDouble("sqrt_result");
			Blob blob = rs.getBlob("similar_term");
			InputStream is = blob.getBinaryStream();
			FastByteArrayOutputStream os = new FastByteArrayOutputStream();
			IoUtil.copyAndClose(is, os);
			byte[] bytes = os.toByteArray();
			Map<String, SimilarTerm> similarTerms = (Map<String, SimilarTerm>)serializeService.deserialize(bytes);
			ContentTerm contentTerm = new ContentTerm(contentId);
			contentTerm.setSimilarTerms(similarTerms);
			contentTerm.setSqrtResult(sqrtResult);
			contentTerm.setTermTotal(termTotal);
			return contentTerm;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

	public void close() {
		SqlUtil.close(rs);
		SqlUtil.close(stmt);
		SqlUtil.close(conn);
	}

}
