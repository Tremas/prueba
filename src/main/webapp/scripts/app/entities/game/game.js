'use strict';

angular.module('pruebaApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('game', {
                parent: 'entity',
                url: '/games',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'pruebaApp.game.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/game/games.html',
                        controller: 'GameController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('game');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('game.detail', {
                parent: 'entity',
                url: '/game/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'pruebaApp.game.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/game/game-detail.html',
                        controller: 'GameDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('game');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Game', function($stateParams, Game) {
                        return Game.get({id : $stateParams.id});
                    }]
                }
            })
            .state('game.new', {
                parent: 'game',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/game/game-dialog.html',
                        controller: 'GameDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    date: null,
                                    localResult: null,
                                    awayResult: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('game', null, { reload: true });
                    }, function() {
                        $state.go('game');
                    })
                }]
            })
            .state('game.edit', {
                parent: 'game',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/game/game-dialog.html',
                        controller: 'GameDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Game', function(Game) {
                                return Game.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('game', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('game.delete', {
                parent: 'game',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/game/game-delete-dialog.html',
                        controller: 'GameDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Game', function(Game) {
                                return Game.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('game', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
