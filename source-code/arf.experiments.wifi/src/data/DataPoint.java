package data;

public class DataPoint
{
	public final int start, length, ID;
	
	DataPoint(int start, int length, int ID)
	{
		this.start  = start;
		this.length = length;
		this.ID     = ID;
	}
	
	public int startBlock(int blockSize)
	{
		return start / blockSize;
	}
	
	public int lengthBlock(int blockSize)
	{
		return length / blockSize;
	}
}
