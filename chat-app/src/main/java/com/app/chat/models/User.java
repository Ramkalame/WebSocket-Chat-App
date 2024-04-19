package com.app.chat.models;

import java.sql.Time;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
	

	private String userId;
	private String userName;
	private UserStatus status;
	private String message;
	private Time messageTime;

}
