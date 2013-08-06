package wint.core.service.definition;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import wint.core.service.ServiceContext;
import wint.lang.magic.MagicList;
import wint.lang.utils.CollectionUtil;


public class ListDefinitionNode extends DefinitionNode {
	
	private List<DefinitionNode> elements;

	public ListDefinitionNode(String name, List<DefinitionNode> elements) {
		this.name = name;
		this.elements = elements;
	}
	
	public ListDefinitionNode(String name, Object elements) {
		this.name = name;
		this.elements = MagicList.wrap(elements);
	}

	public List<DefinitionNode> getElements() {
		return Collections.unmodifiableList(elements);
	}
	
	public void addElement(DefinitionNode element) {
		elements.add(element);
	}
	
	public void addElements(Collection<DefinitionNode> elements) {
		elements.addAll(elements);
	}

	@Override
	public Object createObject(ServiceContext serviceContext, ObjectCreateObsever objectCreateObsever) {
		List<Object> list = CollectionUtil.newArrayList();
		for (DefinitionNode elementNode : elements) {
			Object object = elementNode.createObject(serviceContext, objectCreateObsever);
			list.add(object);
		}
		return list;
	}

	
}
