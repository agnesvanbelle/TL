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

	public static enum FEATURE_TYPE { // used for folder names
		OF(0), HF(1); // Our Features, or Her/Handcrafted Features

		private final int index;

		FEATURE_TYPE(int index) {
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
	


}
