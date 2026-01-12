package com.epiis.app.auxobject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Users {
	public final static List<Map<String, String>> listUser = new ArrayList<>() {
		private static final long serialVersionUID = 1L;

		{
			add(new HashMap<String, String>() {
				private static final long serialVersionUID = 1L;
				{
					put("userName", "kaaf030191");
					put("password", "030191");
				}
			});

			add(new HashMap<String, String>() {
				private static final long serialVersionUID = 1L;
				{
					put("userName", "alice300719");
					put("password", "030191");
				}
			});
		}
	};

	public static Map<String, String> loadByUserName(String userName) {
		return Users.listUser.stream()
				.filter(userMap -> userName.equals(userMap.get("userName")))
				.findFirst().get();
	}
}