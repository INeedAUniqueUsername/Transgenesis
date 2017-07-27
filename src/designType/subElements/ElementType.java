package designType.subElements;

import java.util.function.Supplier;

import xml.DesignElement;

public interface ElementType extends Supplier<DesignElement> {
	public DesignElement get();
}