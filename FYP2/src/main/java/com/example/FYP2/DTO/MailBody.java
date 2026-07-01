package com.example.FYP2.DTO;

import lombok.Builder;

@Builder
public record MailBody(String to, String subjective, String text) {




}
