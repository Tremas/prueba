'use strict';

angular.module('pruebaApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('league', {
                parent: 'entity',
                url: '/leagues',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'pruebaApp.league.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/league/leagues.html',
                        controller: 'LeagueController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('league');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('league.detail', {
                parent: 'entity',
                url: '/league/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'pruebaApp.league.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/league/league-detail.html',
                        controller: 'LeagueDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('league');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'League', function($stateParams, League) {
                        return League.get({id : $stateParams.id});
                    }]
                }
            })
            .state('league.new', {
                parent: 'league',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/league/league-dialog.html',
                        controller: 'LeagueDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('league', null, { reload: true });
                    }, function() {
                        $state.go('league');
                    })
                }]
            })
            .state('league.edit', {
                parent: 'league',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/league/league-dialog.html',
                        controller: 'LeagueDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['League', function(League) {
                                return League.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('league', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('league.delete', {
                parent: 'league',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/league/league-delete-dialog.html',
                        controller: 'LeagueDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['League', function(League) {
                                return League.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('league', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
