'use strict';

angular.module('pruebaApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('coach', {
                parent: 'entity',
                url: '/coachs',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'pruebaApp.coach.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/coach/coachs.html',
                        controller: 'CoachController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('coach');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('coach.detail', {
                parent: 'entity',
                url: '/coach/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'pruebaApp.coach.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/coach/coach-detail.html',
                        controller: 'CoachDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('coach');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Coach', function($stateParams, Coach) {
                        return Coach.get({id : $stateParams.id});
                    }]
                }
            })
            .state('coach.new', {
                parent: 'coach',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/coach/coach-dialog.html',
                        controller: 'CoachDialogController',
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
                        $state.go('coach', null, { reload: true });
                    }, function() {
                        $state.go('coach');
                    })
                }]
            })
            .state('coach.edit', {
                parent: 'coach',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/coach/coach-dialog.html',
                        controller: 'CoachDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Coach', function(Coach) {
                                return Coach.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('coach', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('coach.delete', {
                parent: 'coach',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/coach/coach-delete-dialog.html',
                        controller: 'CoachDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Coach', function(Coach) {
                                return Coach.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('coach', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
