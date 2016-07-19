'use strict';

angular.module('pruebaApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('player', {
                parent: 'entity',
                url: '/players',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'pruebaApp.player.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/player/players.html',
                        controller: 'PlayerController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('player');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('player.detail', {
                parent: 'entity',
                url: '/player/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'pruebaApp.player.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/player/player-detail.html',
                        controller: 'PlayerDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('player');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Player', function($stateParams, Player) {
                        return Player.get({id : $stateParams.id});
                    }]
                }
            })
            .state('player.new', {
                parent: 'player',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/player/player-dialog.html',
                        controller: 'PlayerDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    birthDate: null,
                                    totalBaskets: null,
                                    totalAssists: null,
                                    totalRebounds: null,
                                    fieldPosition: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('player', null, { reload: true });
                    }, function() {
                        $state.go('player');
                    })
                }]
            })
            .state('player.edit', {
                parent: 'player',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/player/player-dialog.html',
                        controller: 'PlayerDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Player', function(Player) {
                                return Player.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('player', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('player.delete', {
                parent: 'player',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/player/player-delete-dialog.html',
                        controller: 'PlayerDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Player', function(Player) {
                                return Player.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('player', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
