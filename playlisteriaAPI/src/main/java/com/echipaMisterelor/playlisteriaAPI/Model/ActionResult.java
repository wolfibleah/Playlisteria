package com.echipaMisterelor.playlisteriaAPI.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class ActionResult {
    private boolean successful;
    private String message;
}
