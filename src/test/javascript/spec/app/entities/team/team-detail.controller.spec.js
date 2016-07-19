'use strict';

describe('Team Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockTeam, MockPlayer, MockStadium, MockCoach, MockPartner, MockSeason, MockGame;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockTeam = jasmine.createSpy('MockTeam');
        MockPlayer = jasmine.createSpy('MockPlayer');
        MockStadium = jasmine.createSpy('MockStadium');
        MockCoach = jasmine.createSpy('MockCoach');
        MockPartner = jasmine.createSpy('MockPartner');
        MockSeason = jasmine.createSpy('MockSeason');
        MockGame = jasmine.createSpy('MockGame');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Team': MockTeam,
            'Player': MockPlayer,
            'Stadium': MockStadium,
            'Coach': MockCoach,
            'Partner': MockPartner,
            'Season': MockSeason,
            'Game': MockGame
        };
        createController = function() {
            $injector.get('$controller')("TeamDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'pruebaApp:teamUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
