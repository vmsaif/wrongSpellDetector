

public class checkHeap {
		
	public static boolean checkHeap( int [] h ) 
	{	
		return checkHeapRecur(h, 0);
	}
	private static Boolean checkHeapRecur(int[] h, int index )
	{
		Boolean out = false;
		if(index > (h.length-1)/2) // parent 
		{
			out = true;
		} else {
			if(h[index] >= h[2*index + 1] && h[index] >= h[2*index + 2]) //left and right child check
			{
				out = checkHeapRecur(h, index+1);
			}
		}
		
		return out;
	}
}

