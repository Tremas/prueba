'use strict';

angular.module('pruebaApp')
	.controller('StadisticsDeleteController', function($scope, $uibModalInstance, entity, Stadistics) {

        $scope.stadistics = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Stadistics.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
