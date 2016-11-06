package data;

/**
 * @author Mocrosoft
 */
public enum World {
	MEMBERS(1131),
	NON_MEMBERS(1130),
	PVP(1237),
	DEADMAN_SEASONAL(1238),
	DEADMAN(1238);
	
	private int texture = 0;
	
	public int getTexture() {
		return texture;
	}
	
	
	private World(int texture) {
		this.texture = texture;
	}
}
