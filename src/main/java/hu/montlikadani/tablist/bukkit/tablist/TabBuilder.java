package hu.montlikadani.tablist.bukkit.tablist;

import java.util.ArrayList;
import java.util.List;

public class TabBuilder {

	private List<String> header;
	private List<String> footer;

	private String permission;

	private boolean random;

	private TabBuilder() {
	}

	static Builder builder() {
		return new Builder();
	}

	public List<String> getHeader() {
		return header;
	}

	public List<String> getFooter() {
		return footer;
	}

	public String getPermission() {
		return permission;
	}

	public boolean isRandom() {
		return random;
	}

	public static class Builder {

		private List<String> header;
		private List<String> footer;

		private String permission = "";

		private boolean random = false;

		protected Builder() {
		}

		public Builder header(List<String> header) {
			this.header = header == null ? new ArrayList<>() : header;
			return this;
		}

		public Builder footer(List<String> footer) {
			this.footer = footer == null ? new ArrayList<>() : footer;
			return this;
		}

		public Builder random(boolean random) {
			this.random = random;
			return this;
		}

		public Builder withPermission(String permission) {
			this.permission = permission == null ? "" : permission;
			return this;
		}

		public TabBuilder build() {
			TabBuilder builder = new TabBuilder();
			builder.header = header;
			builder.footer = footer;
			builder.random = random;
			builder.permission = permission;

			return builder;
		}
	}
}
