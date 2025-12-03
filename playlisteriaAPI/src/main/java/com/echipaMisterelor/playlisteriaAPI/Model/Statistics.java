package com.echipaMisterelor.playlisteriaAPI.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="statistic")
public class Statistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Column(name="action")
    private String action;

    @Column(name="argument")
    private String argument;

    @Column(name="counter")
    private Integer counter;

    public Statistics(String action, String argument, Integer counter) {
        this.action = action;
        this.argument = argument;
        this.counter = counter;
    }
}

