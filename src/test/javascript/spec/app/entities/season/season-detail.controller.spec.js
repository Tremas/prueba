'use strict';

describe('Season Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockSeason, MockTeam, MockLeague, MockGame;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockSeason = jasmine.createSpy('MockSeason');
        MockTeam = jasmine.createSpy('MockTeam');
        MockLeague = jasmine.createSpy('MockLeague');
        MockGame = jasmine.createSpy('MockGame');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Season': MockSeason,
            'Team': MockTeam,
            'League': MockLeague,
            'Game': MockGame
        };
        createController = function() {
            $injector.get('$controller')("SeasonDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'pruebaApp:seasonUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
