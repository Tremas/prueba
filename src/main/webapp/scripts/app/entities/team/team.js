'use strict';

angular.module('pruebaApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('team', {
                parent: 'entity',
                url: '/teams',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'pruebaApp.team.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/team/teams.html',
                        controller: 'TeamController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('team');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('team.detail', {
                parent: 'entity',
                url: '/team/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'pruebaApp.team.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/team/team-detail.html',
                        controller: 'TeamDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('team');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Team', function($stateParams, Team) {
                        return Team.get({id : $stateParams.id});
                    }]
                }
            })
            .state('team.new', {
                parent: 'team',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/team/team-dialog.html',
                        controller: 'TeamDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    creationDate: null,
                                    locality: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('team', null, { reload: true });
                    }, function() {
                        $state.go('team');
                    })
                }]
            })
            .state('team.edit', {
                parent: 'team',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/team/team-dialog.html',
                        controller: 'TeamDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Team', function(Team) {
                                return Team.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('team', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('team.delete', {
                parent: 'team',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/team/team-delete-dialog.html',
                        controller: 'TeamDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Team', function(Team) {
                                return Team.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('team', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
