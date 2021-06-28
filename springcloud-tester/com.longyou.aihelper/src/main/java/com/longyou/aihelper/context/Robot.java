package com.longyou.aihelper.context;

import java.io.IOException;
import java.util.Scanner;


/**
 * 聊天机器人入口
 */
public class Robot {

	private ChartManager chartManager = null;

	public Robot() {
		chartManager = ChartManager.getInstance();
	}




	public String input(String input) throws Exception {
		return chartManager.response(input);
	}




	public static void main(String[] args) throws Exception {
		Robot demo = new Robot();
		Scanner scanner = new Scanner(System.in);

		System.out.println("Alice 已经启动，可以和他对话了");

		String input;
		while ((input = scanner.nextLine()) != null) {
			System.out.println("you say>" + input);
			System.out.println("Alice>" + demo.input(input));
		}
	}


}
