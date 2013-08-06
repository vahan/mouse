package mouse.dataProcessing;

public class BoxData {

	private final String boxId;

	private final String xPos;

	private final String yPos;

	public BoxData(String boxId, String xPos, String yPos) {
		super();
		this.boxId = boxId;
		this.xPos = xPos;
		this.yPos = yPos;
	}

	public String getBoxId() {
		return boxId;
	}

	public String getxPos() {
		return xPos;
	}

	public String getyPos() {
		return yPos;
	}

}
