package wiliberm;

public class Packet {

	private PacketIds id = null;
	private String text = null;

	public Packet(PacketIds id, String text) {
		this.id = id;
		this.text = text;
	}

	public PacketIds getId() {
		return id;
	}

	public void setId(PacketIds id) {
		if (this.id != null)
			return;
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		if (this.text != null)
			return;
		this.text = text;
	}

	private Packet() {
	}

}
