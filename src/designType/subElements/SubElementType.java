package designType.subElements;

import java.util.function.Supplier;

import xml.DesignElementOld;

public interface SubElementType extends Supplier<DesignElementOld> {
	public DesignElementOld get();
}