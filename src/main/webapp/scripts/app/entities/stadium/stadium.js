'use strict';

angular.module('pruebaApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('stadium', {
                parent: 'entity',
                url: '/stadiums',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'pruebaApp.stadium.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/stadium/stadiums.html',
                        controller: 'StadiumController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('stadium');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('stadium.detail', {
                parent: 'entity',
                url: '/stadium/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'pruebaApp.stadium.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/stadium/stadium-detail.html',
                        controller: 'StadiumDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('stadium');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Stadium', function($stateParams, Stadium) {
                        return Stadium.get({id : $stateParams.id});
                    }]
                }
            })
            .state('stadium.new', {
                parent: 'stadium',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/stadium/stadium-dialog.html',
                        controller: 'StadiumDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    measure: null,
                                    maximumCapacity: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('stadium', null, { reload: true });
                    }, function() {
                        $state.go('stadium');
                    })
                }]
            })
            .state('stadium.edit', {
                parent: 'stadium',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/stadium/stadium-dialog.html',
                        controller: 'StadiumDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Stadium', function(Stadium) {
                                return Stadium.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('stadium', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('stadium.delete', {
                parent: 'stadium',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/stadium/stadium-delete-dialog.html',
                        controller: 'StadiumDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Stadium', function(Stadium) {
                                return Stadium.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('stadium', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
