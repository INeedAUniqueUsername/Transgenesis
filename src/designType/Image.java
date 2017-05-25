package designType;

import xml.Attribute;
import xml.Attribute.ValueType;

public class Image extends Type {
	public Image() {
		super();
		initializeImage();
	}
	public Image(String name) {
		super(name);
		initializeImage();
	}
	public void initializeImage() {
		addRequiredAttributes(
				new Attribute("imageID", ValueType.TYPE_IMAGE),
				new Attribute("imageX", ValueType.WHOLE),
				new Attribute("imageY", ValueType.WHOLE),
				new Attribute("imageWidth", ValueType.WHOLE),
				new Attribute("imageHeight", ValueType.WHOLE),
				new Attribute("imageFrameCount", ValueType.WHOLE),
				new Attribute("rotationCount", ValueType.WHOLE),
				new Attribute("rotationColumns", ValueType.WHOLE),
				new Attribute("animationColumns", ValueType.WHOLE),
				new Attribute("imageTicksPerFrame", ValueType.WHOLE),
				new Attribute("flashTicks", ValueType.WHOLE),
				new Attribute("blending", ValueType.BLENDING),
				new Attribute("viewportRatio", ValueType.DECIMAL),
				new Attribute("viewportSize", ValueType.INTEGER),
				new Attribute("rotationOffset", ValueType.INTEGER),
				new Attribute("xOffset", ValueType.INTEGER),
				new Attribute("yOffset", ValueType.INTEGER)
				);
	}
}
