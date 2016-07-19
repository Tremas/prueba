'use strict';

angular.module('pruebaApp')
    .controller('PartnerDetailController', function ($scope, $rootScope, $stateParams, entity, Partner, Team) {
        $scope.partner = entity;
        $scope.load = function (id) {
            Partner.get({id: id}, function(result) {
                $scope.partner = result;
            });
        };
        var unsubscribe = $rootScope.$on('pruebaApp:partnerUpdate', function(event, result) {
            $scope.partner = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
