'use strict';

angular.module('pruebaApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('stadistics', {
                parent: 'entity',
                url: '/stadisticss',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'pruebaApp.stadistics.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/stadistics/stadisticss.html',
                        controller: 'StadisticsController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('stadistics');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('stadistics.detail', {
                parent: 'entity',
                url: '/stadistics/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'pruebaApp.stadistics.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/stadistics/stadistics-detail.html',
                        controller: 'StadisticsDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('stadistics');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Stadistics', function($stateParams, Stadistics) {
                        return Stadistics.get({id : $stateParams.id});
                    }]
                }
            })
            .state('stadistics.new', {
                parent: 'stadistics',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/stadistics/stadistics-dialog.html',
                        controller: 'StadisticsDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    baskets: null,
                                    faults: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('stadistics', null, { reload: true });
                    }, function() {
                        $state.go('stadistics');
                    })
                }]
            })
            .state('stadistics.edit', {
                parent: 'stadistics',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/stadistics/stadistics-dialog.html',
                        controller: 'StadisticsDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Stadistics', function(Stadistics) {
                                return Stadistics.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('stadistics', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('stadistics.delete', {
                parent: 'stadistics',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/stadistics/stadistics-delete-dialog.html',
                        controller: 'StadisticsDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Stadistics', function(Stadistics) {
                                return Stadistics.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('stadistics', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
