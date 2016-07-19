'use strict';

angular.module('pruebaApp').controller('TeamDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Team', 'Player', 'Stadium', 'Coach', 'Partner', 'Season', 'Game',
        function($scope, $stateParams, $uibModalInstance, $q, entity, Team, Player, Stadium, Coach, Partner, Season, Game) {

        $scope.team = entity;
        $scope.players = Player.query();
        $scope.stadiums = Stadium.query({filter: 'team-is-null'});
        $q.all([$scope.team.$promise, $scope.stadiums.$promise]).then(function() {
            if (!$scope.team.stadium || !$scope.team.stadium.id) {
                return $q.reject();
            }
            return Stadium.get({id : $scope.team.stadium.id}).$promise;
        }).then(function(stadium) {
            $scope.stadiums.push(stadium);
        });
        $scope.coachs = Coach.query({filter: 'team-is-null'});
        $q.all([$scope.team.$promise, $scope.coachs.$promise]).then(function() {
            if (!$scope.team.coach || !$scope.team.coach.id) {
                return $q.reject();
            }
            return Coach.get({id : $scope.team.coach.id}).$promise;
        }).then(function(coach) {
            $scope.coachs.push(coach);
        });
        $scope.partners = Partner.query();
        $scope.seasons = Season.query();
        $scope.games = Game.query();
        $scope.load = function(id) {
            Team.get({id : id}, function(result) {
                $scope.team = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('pruebaApp:teamUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.team.id != null) {
                Team.update($scope.team, onSaveSuccess, onSaveError);
            } else {
                Team.save($scope.team, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForCreationDate = {};

        $scope.datePickerForCreationDate.status = {
            opened: false
        };

        $scope.datePickerForCreationDateOpen = function($event) {
            $scope.datePickerForCreationDate.status.opened = true;
        };
}]);
