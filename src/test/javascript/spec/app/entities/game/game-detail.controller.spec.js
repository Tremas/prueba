'use strict';

describe('Game Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockGame, MockSeason, MockReferee, MockTeam, MockStadistics;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockGame = jasmine.createSpy('MockGame');
        MockSeason = jasmine.createSpy('MockSeason');
        MockReferee = jasmine.createSpy('MockReferee');
        MockTeam = jasmine.createSpy('MockTeam');
        MockStadistics = jasmine.createSpy('MockStadistics');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Game': MockGame,
            'Season': MockSeason,
            'Referee': MockReferee,
            'Team': MockTeam,
            'Stadistics': MockStadistics
        };
        createController = function() {
            $injector.get('$controller')("GameDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'pruebaApp:gameUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
