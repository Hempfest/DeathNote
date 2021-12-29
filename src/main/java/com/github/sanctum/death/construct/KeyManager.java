package com.github.sanctum.death.construct;

import com.github.sanctum.labyrinth.library.Deployable;
import java.util.ArrayList;
import java.util.List;

public final class KeyManager {

	private static KeyManager INSTANCE;
	private final List<StringKey> list = new ArrayList<>();

	KeyManager() {}

	public Deployable<Void> register(StringKey key) {
		return Deployable.of(null, unused -> {
			if (!exists(key.getKey())) {
				list.add(key);
			}
		});
	}

	public Deployable<Void> unregister(StringKey key) {
		return Deployable.of(null, unused -> {
			if (exists(key.getKey())) {
				list.remove(key);
			}
		});
	}

	public boolean exists(String key) {
		return list.stream().anyMatch(s -> key.equals(s.getKey()));
	}

	public StringKey get(String key) {
		return list.stream().filter(s -> key.equals(s.getKey())).findFirst().orElse(null);
	}

	public List<StringKey> getKeys() {
		return list;
	}

	public static KeyManager getInstance() {
		return INSTANCE != null ? INSTANCE : (INSTANCE = new KeyManager());
	}

}
