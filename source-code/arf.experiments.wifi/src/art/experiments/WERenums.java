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
	

}
