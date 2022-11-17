# POP1 - Population One Stream Statistics Overview

This statistics is created for my streaming channel (https://twitch.tv/thepaxy/).

![Preview](preview.png)

## Overview features:
- OCR bases Kill/Death counter
- OCR bases Kill/Death with weapon list
- OCR based snapshoot of end-screen results (last round end-screen)
- Statistics API display of session:
  - Games
  - Wins
  - Win percent
  - Damage
  - Damage per game
  - Kills
  - Kills per game
- Statistics API display of current Skill number
- Statistics API display of current MMR number

## Requrements
- Tessaract-OCR - https://tesseract-ocr.github.io/tessdoc/Downloads.html
- org.json - https://jar-download.com/artifacts/org.json
- commons-lang3 - https://commons.apache.org/proper/commons-lang/download_lang.cgi

## Configuration
All configuration should be done in Engine.java file.
Variables:
- player - String player name used by OCR to detect user event. It's recomended to set only few username characters to improve OCR detection accuracy.
- fabUserId - BigBoxVR user id string. Use https://www.pop1stats.com/ to find player fabUserId from URL after /player/
- allowUnknown - Allow display Unknown when weapon type could not be detected by OCR. If false, system will keep trying to detect a weapon which may increase detection inaccuracy.
- guns - list of Guns that can be OCR readed from screen kill log. Its used to improve gun detection accuracy.
- browser - path to Tessaract-OCR. By default, "C:\Program Files\Tesseract-OCR\tesseract.exe".
