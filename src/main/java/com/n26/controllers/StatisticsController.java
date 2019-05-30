package com.n26.controllers;


import com.n26.entities.Statistics;
import com.n26.services.StatisticsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController("/statistics")
public class StatisticsController {
    @Autowired
    private StatisticsService statisticsService;


    /**
     * @return get stats based on transactions happened in last 60 seconds
     */
    @RequestMapping(value = "/statistics", method = RequestMethod.GET)

    public Statistics getStatsSummary(){

        return statisticsService.getSummary();
    }

}
