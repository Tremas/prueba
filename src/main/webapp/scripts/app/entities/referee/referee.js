'use strict';

angular.module('pruebaApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('referee', {
                parent: 'entity',
                url: '/referees',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'pruebaApp.referee.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/referee/referees.html',
                        controller: 'RefereeController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('referee');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('referee.detail', {
                parent: 'entity',
                url: '/referee/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'pruebaApp.referee.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/referee/referee-detail.html',
                        controller: 'RefereeDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('referee');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Referee', function($stateParams, Referee) {
                        return Referee.get({id : $stateParams.id});
                    }]
                }
            })
            .state('referee.new', {
                parent: 'referee',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/referee/referee-dialog.html',
                        controller: 'RefereeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    birthDate: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('referee', null, { reload: true });
                    }, function() {
                        $state.go('referee');
                    })
                }]
            })
            .state('referee.edit', {
                parent: 'referee',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/referee/referee-dialog.html',
                        controller: 'RefereeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Referee', function(Referee) {
                                return Referee.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('referee', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('referee.delete', {
                parent: 'referee',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/referee/referee-delete-dialog.html',
                        controller: 'RefereeDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Referee', function(Referee) {
                                return Referee.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('referee', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
