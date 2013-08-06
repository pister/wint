package wint.lang.misc.profiler;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wint.lang.utils.StringUtil;
import wint.lang.utils.SystemUtil;

/**
 *
 * @author pister 2010-2-22
 */
public class Profiler {

	private static final Logger log = LoggerFactory.getLogger(Profiler.class);
	
	private static final ThreadLocal<Entity> entityStack = new ThreadLocal<Entity>();
	
	private static final String LINE_SEP = SystemUtil.LINE_SEPARATOR;
	
	public static void start(String message) {
		setCurrentEntity(new Entity(message, null));
	}
	
	public static void enter(String message) {
		Entity entity = getCurrentEntity();
		if (entity == null) {
			return;
		}
		setCurrentEntity(new Entity(message, entity));
	}
	
	public static void release(String message) {
		Entity entity = getCurrentEntity();
		if (entity == null) {
			return;
		}
		entity.release();
		if (entity.getParent() != null) {
			setCurrentEntity(entity.getParent());
		}
	}
	
	public static void reset() {
		entityStack.remove();
	}
	
	public static void enter() {
		enter(null);
	}
	
	public static void release() {
		release(null);
	}
	
	public static void logMessage(long minTimes) {
		if (!log.isWarnEnabled()) {
			return;
		}
		String msg = getMessage(minTimes);
		if (!StringUtil.isEmpty(msg)) {
			log.warn(msg);
		}
	}
	
	public static String getMessage(long minTimes) {
		Entity e = getRootEntity();
		if (e == null) {
			return StringUtil.EMPTY;
		}
		long espace = e.escape();
		if (espace < minTimes) {
			return StringUtil.EMPTY;
		}
		String ret = StringUtil.EMPTY;
		if (e != null) {
			ret = LINE_SEP + e.toString(0);
		}
		return ret;
	}
	
	private static Entity getRootEntity() {
		Entity e = getCurrentEntity();
		if (e == null) {
			return null;
		}
		Entity rootEntity = e;
		while (e != null) {
			e = e.getParent();
			if (e == null) {
				break;
			}
			rootEntity = e;
		}
		return rootEntity;
	}
	
	private static Entity getCurrentEntity() {
		return entityStack.get();
	}
	
	private static void setCurrentEntity(Entity entity) {
		entityStack.set(entity);
	}
	
	private static class Entity {
		
		private String startMessage;
		
		private long start = 0;
		
		private long end = 0;
		
		private Entity parent;
		
		private List<Entity> children = new ArrayList<Entity>();

		public Entity(String message, Entity parent) {
			this.startMessage = message;
			this.parent = parent;
			if (parent != null) {
				parent.children.add(this);
			}
			start = System.currentTimeMillis();
		}

		public void release() {
			end = System.currentTimeMillis();
		}
		
		public long escape() {
			if (end < start) {
				return -1;
			}
			return end - start;
		}
		
		private long escapeSelf() {
			long ret = escape();
			if (ret < 0) {
				return -1;
			}
			for (Entity e : children) {
				long childEscape = e.escape();
				if (childEscape < 0) {
					continue;
				}
				ret -= childEscape;
			}
			return ret;
		}
		
		private long selfPercentOfAll() {
			Entity root = getRootEntity();
			if (root == null) {
				return 100;
			}
			long escape = escapeSelf();
			long escapeRoot = root.escape();
			if (escape < 0 || escapeRoot <= 0) {
				return 0;
			}
			return 100 * escape / escapeRoot;
		}
		
		public long percentOfAll() {
			if (parent == null) {
				return 100;
			}
			Entity root = getRootEntity();
			if (root == null) {
				return 100;
			}
			long escape = escape();
			long escapeRoot = root.escape();
			if (escape < 0 || escapeRoot <= 0) {
				return 0;
			}
			return 100 * escape / escapeRoot;
		}
		
		public String toString(int intend) {
			StringBuilder sb = new StringBuilder();
			long escape = escape();
			long escapeSelf = escapeSelf();
			appendIntends(sb, intend);
			sb.append(escape);
			sb.append("ms, ");
			sb.append(escapeSelf);
			sb.append("ms ");
			sb.append("[" + percentOfAll() + "%, "+ selfPercentOfAll() +"%] ");
			if (startMessage != null) {
				sb.append(startMessage);
			}
			sb.append(LINE_SEP);
			for (Entity e : children) {
				long childEscape = e.escape();
				if (childEscape < 0) {
					continue;
				}
				sb.append(e.toString(intend + 1));
			}
			return sb.toString();
		}
		
		private static void appendIntends(StringBuilder sb, int intends) {
			final int size = 4 * intends;
			for (int i = 0; i < size; ++i) {
				sb.append(' ');
			}
		}

		public Entity getParent() {
			return parent;
		}

	}
	
}
