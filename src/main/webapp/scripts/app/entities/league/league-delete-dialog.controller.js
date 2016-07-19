'use strict';

angular.module('pruebaApp')
	.controller('LeagueDeleteController', function($scope, $uibModalInstance, entity, League) {

        $scope.league = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            League.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
