Documentation Application Mobile BDF

Informations complémentaires:
Application utilisant la librairie Tehras chart pour afficher les graphiques.
Modèle utilisé : MVVM
API utilisée: Webstat (Banque de France)
Charte graphique: Jaune et bleu (charte graphique de la BDF).
Elément de notation: L’application ne comporte certe pas d’image provenant de l’API mais j’espère que l’usage de charts (qui fut assez difficile surtout pour trouver une librairie convenable) permet de montrer l’usage de quelque chose qui n’était pas montré dans le cours et donc, montre mon implication.


Améliorations non-faites à défaut de simplicité:
Centralisation et anonymisation de la clé d’API.
Debouncer pour les recherches.
Augmentation du nombre de valeurs affichées ( si vous voulez, il suffit d’augmenter la limite de champs requétés dans les fichiers repository puisque les URL de requêtes prennent aussi en paramètres des mots clés SQL), actuellement l’application est automatisée pour pouvoir handle autant que données que demandée à défaut du temps d’attente pour l’affichage.
