package util;

public final class VectorKey {
	final int a;
	final int b;
	final int c;

	VectorKey(int a, int b, int c) {
		this.a = a;
		this.b = b;
		this.c = c;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof VectorKey)) {
			return false;
		}

		VectorKey that = (VectorKey) obj;
		return (this.a == that.a) && (this.b == that.b) && (this.c == that.c);
	}

	@Override
	public int hashCode() {
		return a << 42 | b << 21 | c;
	}
}
