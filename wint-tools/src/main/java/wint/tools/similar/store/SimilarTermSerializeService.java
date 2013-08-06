package wint.tools.similar.store;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import wint.tools.similar.proccess.ContentTerm;
import wint.tools.similar.proccess.SimilarTerm;
import wint.tools.util.CollectionUtil;
import wint.tools.util.FastByteArrayInputStream;
import wint.tools.util.FastByteArrayOutputStream;

public class SimilarTermSerializeService {
	
	public void serializeContentTerm(ContentTerm contentTerm, OutputStream os) {
		DataOutputStream dos = new DataOutputStream(os);
		try {
			// version 1
			dos.writeInt(1);
			dos.writeUTF(contentTerm.getId());
			dos.writeInt(contentTerm.getTermTotal());
			dos.writeDouble(contentTerm.getSqrtResult());
			dos.write(serialize(contentTerm.getSimilarTerms()));
			dos.flush();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public byte[] serializeContentTerm(ContentTerm contentTerm) {
		FastByteArrayOutputStream bos = new FastByteArrayOutputStream();
		serializeContentTerm(contentTerm, bos);
		return bos.toByteArray();
	}
	
	public ContentTerm deserializeContentTerm(InputStream is) {
		DataInputStream dis = new DataInputStream(is);
		try {
			@SuppressWarnings("unused")
			int version = dis.readInt();
			// ignore
			String id = dis.readUTF();
			int termTotal = dis.readInt();
			double sqrtResult = dis.readDouble();
			Map<String, SimilarTerm> similarTerms = deserialize(dis);
			
			ContentTerm ret = new ContentTerm();
			ret.setId(id);
			ret.setTermTotal(termTotal);
			ret.setSqrtResult(sqrtResult);
			ret.setSimilarTerms(similarTerms);			
			return ret;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public ContentTerm deserializeContentTerm(byte[] input) {
		return deserializeContentTerm(new FastByteArrayInputStream(input));
	}
	
	
	
	public byte[] serialize(Map<String, SimilarTerm> similarTerms) {
		FastByteArrayOutputStream bos = new FastByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		try {
			// count;
			dos.writeInt(similarTerms.size());
			
			for (Map.Entry<String, SimilarTerm> entry : similarTerms.entrySet()) {
				SimilarTerm similarTerm = entry.getValue();
				String term = similarTerm.getTerm();
				int count = similarTerm.getCount();
				double tf = similarTerm.getTf();
				double idf = similarTerm.getIdf();
				double tfidf = similarTerm.getTfidf();
				
				dos.writeUTF(term);
				dos.writeInt(count);
				dos.writeDouble(tf);
				dos.writeDouble(idf);
				dos.writeDouble(tfidf);
			}
			dos.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return bos.toByteArray();
	}
	
	public Map<String, SimilarTerm> deserialize(InputStream is) {
		DataInputStream dis = new DataInputStream(is);
		try {
			int count = dis.readInt();
			Map<String, SimilarTerm> ret = CollectionUtil.newHashMap();
			for (int i = 0; i < count; ++i) {
				String term = dis.readUTF();
				int termCount = dis.readInt();
				double tf = dis.readDouble();
				double idf = dis.readDouble();
				double tfidf = dis.readDouble();
				SimilarTerm similarTerm = new SimilarTerm();
				similarTerm.setTerm(term);
				similarTerm.setCount(termCount);
				similarTerm.setTf(tf);
				similarTerm.setIdf(idf);
				similarTerm.setTfidf(tfidf);
				ret.put(term, similarTerm);
			}
			return ret;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public Map<String, SimilarTerm> deserialize(byte[] input) {
		return deserialize(new FastByteArrayInputStream(input));
	}
	

}
