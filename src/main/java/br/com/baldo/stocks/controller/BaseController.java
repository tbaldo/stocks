package br.com.baldo.stocks.controller;

import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(BaseController.BASE_PATH)
public abstract class BaseController {

    public static final String BASE_PATH = "/financial/v1";
}