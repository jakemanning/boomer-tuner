package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Playlist implements Category, Serializable {
	private String name;
	private List<Playable> items = new ArrayList<>();

	public Playlist(final String name, List<? extends Playable> items) {
		// TODO: Ensure no duplicate playlist names
		this.name = name;
		this.items.addAll(items);
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Playlist playlist = (Playlist) o;

		return Objects.equals(name, playlist.name) && Objects.equals(items, playlist.items);
	}

	@Override
	public int hashCode() {
		int result = name.hashCode();
		result = 31 * result + items.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return name;
	}

	public String getName() { return name; }

	public List<Playable> getItems() {
		return items;
	}
}
