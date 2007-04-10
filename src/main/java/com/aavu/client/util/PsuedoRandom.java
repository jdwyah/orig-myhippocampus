package com.aavu.client.util;

public interface PsuedoRandom {
	int nextInt(int max);
	double nextDouble();
	void reInit();
}
