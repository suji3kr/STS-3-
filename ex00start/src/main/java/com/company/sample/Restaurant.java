package com.company.sample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class Restaurant {
	@Autowired       /* 스프링 컨테이너에 주입 */
	private Chef chef;
}
