'use strict';

angular.module('pruebaApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('partner', {
                parent: 'entity',
                url: '/partners',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'pruebaApp.partner.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/partner/partners.html',
                        controller: 'PartnerController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('partner');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('partner.detail', {
                parent: 'entity',
                url: '/partner/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'pruebaApp.partner.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/partner/partner-detail.html',
                        controller: 'PartnerDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('partner');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Partner', function($stateParams, Partner) {
                        return Partner.get({id : $stateParams.id});
                    }]
                }
            })
            .state('partner.new', {
                parent: 'partner',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/partner/partner-dialog.html',
                        controller: 'PartnerDialogController',
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
                        $state.go('partner', null, { reload: true });
                    }, function() {
                        $state.go('partner');
                    })
                }]
            })
            .state('partner.edit', {
                parent: 'partner',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/partner/partner-dialog.html',
                        controller: 'PartnerDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Partner', function(Partner) {
                                return Partner.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('partner', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('partner.delete', {
                parent: 'partner',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/partner/partner-delete-dialog.html',
                        controller: 'PartnerDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Partner', function(Partner) {
                                return Partner.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('partner', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
