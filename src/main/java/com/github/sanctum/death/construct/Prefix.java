package com.github.sanctum.death.construct;

public interface Prefix {

	String start();

	String middle();

	String end();

	class Builder {

		private String start;
		private String middle;
		private String end;

		public Builder start(String text) {
			this.start = text;
			return this;
		}

		public Builder middle(String text) {
			this.middle = text;
			return this;
		}

		public Builder end(String text) {
			this.end = text;
			return this;
		}

		public Prefix build() {
			return new Prefix() {
				@Override
				public String start() {
					return start;
				}

				@Override
				public String middle() {
					return middle;
				}

				@Override
				public String end() {
					return end;
				}
			};
		}

		@Override
		public String toString() {
			if (start != null && middle != null && end != null) {
				return start + middle + end;
			}
			if (start != null && middle != null) {
				return start + middle;
			}
			if (start != null) {
				return start;
			}
			return super.toString();
		}
	}

}
