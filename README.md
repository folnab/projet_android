# Application E-commerce Android - TP Kotlin

**Binôme :**
- Paul de Buretel de Chassey
- Alban Flouvat

## Description du Projet

Application mobile de commerce électronique développée en Kotlin sur plateforme Android. L'application exploite l'API REST FakeStore pour fournir une expérience complète de shopping mobile avec fonctionnalités de recherche, scan QR et gestion de panier.

## Objectif

Développer une application mobile de e-commerce permettant à l'utilisateur de :
- Rechercher un article via une chaîne de caractères avec affichage de la liste de résultats
- Afficher la fiche détaillée d'un article
- Scanner un QRCode contenant l'identifiant d'un article et visualiser sa fiche correspondante
- Mettre un article dans le panier avec gestion des quantités
- Afficher et gérer le panier complet

## Source des Données

Les données concernant les articles et la gestion du panier sont accessibles via l'API REST :
**https://fakestoreapi.com/**


## Architecture

### Structure des Activités

#### MainActivity
**Fonctionnalités principales :**
- Interface d'accueil avec liste de produits organisée en grille
- Barre de recherche avec autocomplétion en temps réel
- Navigation par catégories dynamiques
- Filtrage des produits selon les critères de recherche
- Ajout au panier depuis la grille de produits
- Navigation bottom avec accès aux fonctions principales

**Implémentation technique :**
- RecyclerView pour l'affichage optimisé des produits
- TextWatcher pour la recherche en temps réel
- ArrayAdapter pour l'autocomplétion
- Integration avec ProductRepository pour l'accès aux données API

#### ProductDetailActivity
**Fonctionnalités :**
- Affichage complet des informations produit (image, titre, prix, catégorie, description)
- Bouton d'ajout au panier avec feedback utilisateur
- Génération et affichage de QR code pour le produit
- Navigation retour vers l'écran précédent

**Implémentation technique :**
- Chargement d'images via Glide avec gestion d'erreurs
- Integration avec CartManager pour ajout au panier
- Génération QR code via ZXing

#### CartActivity
**Fonctionnalités :**
- Affichage détaillé de tous les articles du panier
- Modification des quantités par article (augmentation/diminution) avec calcul automatique prix_article * quantité
- Suppression d'articles individuels ou vidage complet du panier
- Calcul automatique du prix total
- Interface adaptative selon l'état du panier (vide/rempli)
- Processus de validation de commande

**Implémentation technique :**
- CartAdapter personnalisé pour RecyclerView
- Gestion des callbacks pour mise à jour UI
- Integration avec CartManager pour persistance des données

#### QrScannerActivity
**Fonctionnalités :**
- Scanner QR code via caméra
- Gestion des permissions caméra avec demandes utilisateur
- Reconnaissance automatique des codes au format PRODUCT_ID:xxx
- Redirection automatique vers ProductDetailActivity
- Gestion d'erreurs pour codes non reconnus

**Implémentation technique :**
- ZXingScannerView pour interface de scan
- ResultHandler pour traitement des résultats
- Gestion lifecycle pour démarrage/arrêt caméra
- Validation format et extraction ID produit

#### QRCodeActivity
**Fonctionnalités :**
- Génération dynamique de QR codes pour produits
- Affichage QR code
- Bouton pour lancer le scanner depuis la même interface
- Navigation retour vers écran précédent

**Implémentation technique :**
- QRCodeWriter pour génération de codes
- Format standardisé PRODUCT_ID:xxx
- Conversion bitmap pour affichage ImageView

### Composants de Données

#### CartManager (Singleton)
**Responsabilités :**
- Gestion globale et persistante du panier durant la session
- Ajout, suppression et modification des quantités
- Calcul automatique des prix totaux
- Fourniture de données observables pour l'UI

#### CartAdapter
**Fonctionnalités :**
- Adapter personnalisé pour RecyclerView du panier
- Gestion des interactions utilisateur (quantités, suppression)
- Mise à jour dynamique de l'affichage
- Callbacks pour synchronisation avec CartManager

## Fonctionnalités supplémentaires

### Création de compte
- Formulaire d'inscription avec saisie username, email, password
- Appel à l'API POST /users via UserService pour l’enregistrement
- Affichage de feedback en cas de succès ou d’erreur
- Redirection vers la page de connexion après validation

#### Implémentation technique :
- Formulaire intégré dans RegisterActivity avec validation de saisie
- Repository UserRepository pour encapsuler les appels Retrofit
- Utilisation de LiveData pour la réponse de l’API
- Gestion de l’état de session via SharedPreferences

### Connexion
- Formulaire de connexion avec authentification par username et password
- Appel à l’API POST /auth/login et récupération du token
- Enregistrement de la session locale (is_logged_in, user_id, username, email)
- Redirection automatique vers la page d’accueil en cas de succès

#### Implémentation technique :
- LoginActivity avec appels à AuthRepository et AuthService
- Observation LiveData pour affichage dynamique du résultat de connexion
- Sauvegarde des informations de session dans SharedPreferences
- Navigation conditionnelle basée sur l'état de connexion

### Profil utilisateur
- Accès au profil depuis le menu Account si l’utilisateur est connecté
- Affichage du username et de l’email dans ProfileActivity
- Affichage conditionnel du menu popup :
- Si connecté → Profil, Déconnexion
- Si non connecté → Connexion
- Déconnexion possible via le menu, avec suppression des données de session

#### Implémentation technique :
- BaseActivity abstraite pour centraliser le layout commun (header, nav bar, FAB)
- Injection dynamique du contenu via setContentLayout() dans les activités enfants
- Utilisation de SharedPreferences pour stocker et récupérer l’état de session
- Menu dynamique via PopupMenu selon l’état utilisateur

## Permissions et Fonctionnalités

### Permissions Requises
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.CAMERA" />
```

### Fonctionnalités Matérielles
```xml
<uses-feature android:name="android.hardware.camera" android:required="false" />
```

## Installation et Déploiement

### Prérequis
- Android Studio Arctic Fox ou version ultérieure
- SDK Android API 34 minimum
- Appareil ou émulateur avec caméra pour fonctions QR
- Connexion Internet pour accès API

### Configuration
1. Cloner le repository
2. Ouvrir dans Android Studio
3. Synchroniser les dépendances Gradle
4. Configurer appareil de test ou émulateur
5. Build et déploiement via Android Studio

