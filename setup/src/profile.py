from typing import Callable, Tuple


def generate_scoring_function(liked_ingredients,
        disliked_ingredients,
        scoring_function):

	def _contains(ingredient: str, ingredients: list[str]) -> int:
		return any(ing in ingredient['name'] for ing in ingredients)
	
	calculate_score = lambda ingredient: 1 * _contains(ingredient, liked_ingredients) + \
		-1 * _contains(ingredient, disliked_ingredients) 

	return lambda recipe: sum(calculate_score(ing) for ing in recipe['ingredients'])


class Profile:
	def __init__(self, liked_ingredients: list[str] , 
		disliked_ingredients: list[str], 
		scoring_func=None):

		self.liked_ingredients = liked_ingredients
		self.disliked_ingredients = disliked_ingredients
		self.scoring_function = self._generate_scoring_function(scoring_func)

	def _generate_scoring_function(self, scoring_func=None)-> Callable: 
		return generate_scoring_function(
			self.liked_ingredients,
			self.disliked_ingredients,
			scoring_func or (lambda recipe: 0))

	def score_recipes(self, recipes: list[dict]) -> list[Tuple[dict, int]]:
		return [(self.scoring_function(recipe), recipe) for recipe in recipes]

