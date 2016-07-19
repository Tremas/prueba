'use strict';

angular.module('pruebaApp')
	.controller('PartnerDeleteController', function($scope, $uibModalInstance, entity, Partner) {

        $scope.partner = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Partner.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
