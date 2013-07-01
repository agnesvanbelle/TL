package art.experiments;

public class WERenums {
	
	public static enum SET_TYPE { // used for folder names
		TEST(0), TRAIN(1); // test, or train data

		private final int index;

		SET_TYPE(int index) {
			this.index = index;
		}

		public int index() {
			return index;
		}

		public static int length() {
			return values().length;
		}
	
	}
	

	
	public static enum TRANSFER_TYPE { // used for folder names
		TRANSFER(0), NOTRANSFER(1); // transfer, or no-transfer

		private final int index;

		
		public static String[] valuesStr() {
			TRANSFER_TYPE[] vs = TRANSFER_TYPE.values();
			String[] v = new String[TRANSFER_TYPE.length()];
			int i=0;
			for (TRANSFER_TYPE pt : vs) {
				v[i++]=pt.name();
			}
			return v;
		}
		
		TRANSFER_TYPE(int index) {
			this.index = index;
		}

		public int index() {
			return index;
		}

		public static int length() {
			return values().length;
		}
	}

	public static enum MMF_TYPE { // used for folder names
		AUTO(0), HC_MMF(1); // Our Features, or Her/Handcrafted Features

		private final int index;

		MMF_TYPE(int index) {
			this.index = index;
		}

		public int index() {
			return index;
		}

		public static int length() {
			return values().length;
		}
	}
	
	public static enum CLUSTER_TYPE { 
		CT_ABS(0), CT_REL(1); 

		private final int index;

		public static String[] valuesStr() {
			CLUSTER_TYPE[] vs = CLUSTER_TYPE.values();
			String[] v = new String[CLUSTER_TYPE.length()];
			int i=0;
			for (CLUSTER_TYPE pt : vs) {
				v[i++]=pt.name();
			}
			return v;
		}
		
		CLUSTER_TYPE(int index) {
			this.index = index;
		}

		public int index() {
			return index;
		}

		public static int length() {
			return values().length;
		}
	}
	
	public static enum PROFILE_TYPE { 
		PR_SP(0), PR_BOTH(1); 

		public static String[] valuesStr() {
			PROFILE_TYPE[] vs = PROFILE_TYPE.values();
			String[] v = new String[PROFILE_TYPE.length()];
			int i=0;
			for (PROFILE_TYPE pt : vs) {
				v[i++]=pt.name();
			}
			return v;
		}
		
		private final int index;

		PROFILE_TYPE(int index) {
			this.index = index;
		}

		public int index() {
			return index;
		}

		public static int length() {
			return values().length;
		}
	}
	
	public static enum DISTANCE_MEASURE { 
		SSE(0), KL(1); 

		public static String[] valuesStr() {
			DISTANCE_MEASURE[] vs = DISTANCE_MEASURE.values();
			String[] v = new String[PROFILE_TYPE.length()];
			int i=0;
			for (DISTANCE_MEASURE pt : vs) {
				v[i++]=pt.name();
			}
			return v;
		}
		
		private final int index;

		DISTANCE_MEASURE(int index) {
			this.index = index;
		}

		public int index() {
			return index;
		}

		public static int length() {
			return values().length;
		}
	}
	
	
	public static enum MF_TYPE { 
		HC(0), AUTO(1); 

		public static String[] valuesStr() {
			MF_TYPE[] vs = MF_TYPE.values();
			String[] v = new String[MF_TYPE.length()];
			int i=0;
			for (MF_TYPE pt : vs) {
				v[i++]=pt.name();
			}
			return v;
		}
		
		private final int index;

		MF_TYPE(int index) {
			this.index = index;
		}

		public int index() {
			return index;
		}

		public static int length() {
			return values().length;
		}
	}
	
	public static enum TRANSFER_SETTINGS { 
		ONLY_TRANSFER(0), ONLY_NONTRANSFER(1), BOTH(2); 

		public static String[] valuesStr() {
			TRANSFER_SETTINGS[] vs = TRANSFER_SETTINGS.values();
			String[] v = new String[TRANSFER_SETTINGS.length()];
			int i=0;
			for (TRANSFER_SETTINGS pt : vs) {
				v[i++]=pt.name();
			}
			return v;
		}
		
		private final int index;

		TRANSFER_SETTINGS(int index) {
			this.index = index;
		}

		public int index() {
			return index;
		}

		public static int length() {
			return values().length;
		}
	}
	
	
	
	

}
