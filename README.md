Alle wichtigen Dateien: /src

Einfach runterladen und als neues Projekt erstellen

Git-push:
Falls keine 2FA eingestellt ist, könnt ihr euch per HTTP & Terminal verbinden und alles hochladen. Sonst müsstet ihr eine SSH Verbindung einrichten
(-> https://docs.github.com/en/authentication/connecting-to-github-with-ssh)

Falls erfolgt, funktioniert es bei mir mit den Befehlen:
1. im Terminal in das Verzeichnis der Dateien gehen
2. git init -> erstellt eine leere Repository für git push
3. git add . -> alle Dateien auswählen
4. git commit -m "dies ist ein Text" -> kurzen Text über updates schreiben
5. git push -u origin main ODER git push -> die Dateien hochladen

   Per 'git status' aktuellen Status einsehen, 'git branch' aktuelle Repository (?)

To-Dos:
- [ ] einfach ALLES nochmal überarbeiten -> sauberer und verständlicher gestalten
- [ ] Spielfeld beschriftung hinzufügen (1-8 und a-h)
- [X] Spieler nur abwechselnd bewegen lassen (weiß, schwarz, weiß,...)
- [ ] Prüfen ob Spiel zu ende ist
- [ ] "Endscreen" - wenn jemand gewonnen hat
- [ ] "erworbene" Figuren anzeigen - Weiß hat x Steine und y Damen geschlagen, Schwarz hat
- [X] Spielspeicherung
- [X] Bauer können nach unten und oben platziert werden (in 'checkBauerBewegen()')
- [X] Damen können die eigene Farbe schlagen
...
