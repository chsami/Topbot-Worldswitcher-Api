package data;


/**
 * @author Mocrosoft
 */
public class WorldSettings {
	
	/**
	 * Array of worlds that are aloud
	 */
	private static World[] worldType = new World[]{};
	
	/**
	 * getter
	 * @return
	 */
	public static World[] getWorldType() {
		return worldType;
	}
	
	/**
	 * setter
	 * @param worldType
	 */
	public static void setWorldType(World ... types) {
		WorldSettings.worldType = types;
	}
	
}
