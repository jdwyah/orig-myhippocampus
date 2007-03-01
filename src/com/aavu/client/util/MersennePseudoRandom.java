package com.aavu.client.util;

public class MersennePseudoRandom implements PsuedoRandom {

	int[] state = new int[624];
	
	public MersennePseudoRandom(int seed){
//		 Create a length 624 array to store the state of the generator
//		 var int[0..623] MT
//		 var int y
//		 // Initialise the generator from a seed
//		 function initialiseGenerator ( 32-bit int seed ) {
//		     MT[0] := seed
//		     for i from 1 to 623 { // loop over each other element
//		         MT[i] := last_32bits_of((69069 * MT[i-1]) + 1) // 69069 == 0x10dcd
//		     }
//		 }
		 
		 state[0] = seed;
		 for(int i = 1;i < 624; i++){
			 state[i] = (69069 * state[i-1]) + 1;
		 }

		 // Generate an array of 624 untempered numbers
//		 function generateNumbers() {
//		     for i from 0 to 622 {
//		         y := 32nd_bit_of(MT[i]) + last_31bits_of(MT[i+1])
//		         if y even {
//		             MT[i] := MT[(i + 397) % 624] bitwise xor (right_shift_by_1_bit(y))
//		         } else if y odd {
//		             MT[i] := MT[(i + 397) % 624] bitwise_xor (right_shift_by_1_bit(y)) bitwise_xor (2567483615)
//		         }
//		     }
//		     y := 32nd_bit_of(MT[623]) + last_31bits_of(MT[0])
//		     if y even {
//		         MT[623] := MT[396] bitwise_xor (right_shift_by_1_bit(y))
//		     } else if y odd {
//		         MT[623] := MT[396] bitwise_xor (right_shift_by_1_bit(y)) bitwise_xor (2567483615) // 0x9908b0df
//		     }
//		 }
		 
//		 for(int i = 0; i < 622; i++){
//			 y = 
//		 }
		 
		 
		 throw new UnsupportedOperationException();

	}
	
	public double nextDouble() {
		
		 // Extract a tempered pseudorandom number based on the i-th value
//		 function extractNumber(int i) {
//		     y := MT[i]
//		     y := y bitwise_xor (right_shift_by_11_bits(y))
//		     y := y bitwise_xor (left_shift_by_7_bits(y) bitwise_and (2636928640)) // 0x9d2c5680
//		     y := y bitwise_xor (left_shift_by_15_bits(y) bitwise_and (4022730752)) // 0xefc60000
//		     y := y bitwise_xor (right_shift_by_18_bits(y))
//		     return y
//		 }
		
		// TODO Auto-generated method stub
		return 0;
	}

	public int nextInt(int max) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void reInit() {
		// TODO Auto-generated method stub

	}

}
