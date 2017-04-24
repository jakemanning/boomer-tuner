package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Playlist implements Category {
	private String name;
	private List<Playable> items = new ArrayList<>();

	public Playlist(final String name, List<? extends Playable> items) {
		this.name = name;
		this.items.addAll(items);
	}

	@Override
	public boolean equals(Object o) {
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
